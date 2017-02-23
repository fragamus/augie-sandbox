(ns untangled-todomvc.i18n.es-MX (:require untangled.i18n.core) (:import goog.module.ModuleManager))

;; This file was generated by untangled's i18n leiningen plugin.

(def
 translations
 {"|Created by " "Creado por",
  "|Completed" "Completo",
  "|<===>" "",
  "|Active" "Activo",
  "|Frame {f,number} of {end,number} "
  "Marco {f,number} of {end,number} ",
  "|Double-click to edit a todo" "Haga doble clic para editar un TODO",
  "|What needs to be done?" "¿Qué es lo que se debe hacer?",
  "|All" "Todas",
  "|Help!" "¡Ayuda!",
  "|<Back" "Atrás",
  "|{num,plural,=0 {no items} =1 {1 item} other {# items}} left"
  "{num,plural,=0 {no queda nada} =1 {1 artículo restante} other {# artículos}}",
  "|{ts,date,short} {ts,time,long}" "",
  "|Mark all as complete" "Marcar todo como completa",
  "|Send Request" "Enviar petición",
  "|Forward>" "Adelante >",
  "|Clear Completed" "Despejado completo"})

(swap!
 untangled.i18n.core/*loaded-translations*
 (fn [x] (assoc x "es-MX" translations)))

(try
 (-> goog.module.ModuleManager .getInstance (.setLoaded "es-MX"))
 (catch js/Object obj))
