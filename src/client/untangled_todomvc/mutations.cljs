(ns untangled-todomvc.mutations
  (:require [untangled.client.mutations :as m]
            [untangled.dom :refer [unique-key]]
            [untangled.client.core :as uc]
            [untangled-todomvc.core :as core]))

(defmethod m/mutate 'support-viewer/send-support-request [{:keys [ast state]} k {:keys [comment]}]
  {:remote (assoc ast :params {:comment comment :history (uc/history @core/app)})})

(defmethod m/mutate 'support/toggle [{:keys [state]} k p]
  {:action (fn [] (swap! state update :ui/support-visible not))})

(defmethod m/mutate 'todo/new-item [{:keys [state ast]} _ {:keys [id text]}]
  {:remote (assoc ast :params {:id id :text text :list (:list @state)})
   :action (fn []
             (swap! state #(-> %
                               (update-in [:todos :list/items] (fn [todos] ((fnil conj []) todos [:todo/by-id id])))
                               (assoc-in [:todo/by-id id] {:db/id id :item/label text}))))})

(defmethod m/mutate 'todo/check [{:keys [state]} _ {:keys [id]}]
  {:remote true
   :action (fn [] (swap! state assoc-in [:todo/by-id id :item/complete] true))})

(defmethod m/mutate 'todo/uncheck [{:keys [state]} _ {:keys [id]}]
  {:remote true
   :action (fn [] (swap! state assoc-in [:todo/by-id id :item/complete] false))})

(defmethod m/mutate 'todo/edit [{:keys [state]} _ {:keys [id text]}]
  {:remote true
   :action (fn [] (swap! state assoc-in [:todo/by-id id :item/label] text))})

(defmethod m/mutate 'todo/delete-item [{:keys [state]} _ {:keys [id]}]
  {:remote true
   :action (fn []
             (letfn [(remove-item [todos] (vec (remove #(= id (second %)) todos)))]
               (swap! state #(-> %
                                 (update-in [:todos :list/items] remove-item)
                                 (update :todo/by-id dissoc id)))))})

(defn- set-completed [val todos]
  (into {} (map (fn [[k v]] [k (assoc v :item/complete val)]) todos)))

(defmethod m/mutate 'todo/check-all [{:keys [ast state]} _ _]
  {:remote (assoc ast :params {:id (:list @state)})
   :action (fn [] (swap! state update :todo/by-id (partial set-completed true)))})

(defmethod m/mutate 'todo/uncheck-all [{:keys [ast state]} _ _]
  {:remote (assoc ast :params {:id (:list @state)})
   :action (fn [] (swap! state update :todo/by-id (partial set-completed false)))})

(defmethod m/mutate 'todo/clear-complete [{:keys [ast state]} _ _]
  {:remote (assoc ast :params {:id (:list @state)})
   :action (fn []
             (swap! state (fn [st]
                            (-> st
                                (update-in [:todos :list/items]
                                           (fn [todos] (vec (remove (fn [[_ id]] (get-in @state [:todo/by-id id :item/complete] false)) todos))))))))})

(defmethod m/mutate 'todo/filter [{:keys [ast state]} _ {:keys [filter]}]
  {:action (fn [] (swap! state assoc-in [:todos :list/filter] filter))})
