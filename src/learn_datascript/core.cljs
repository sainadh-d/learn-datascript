(ns learn-datascript.core
  (:require [reagent.dom :refer [render]]))

(defn hello-world []
  [:div
   [:h3 "Edit this and watch it change!"]])

(defn start []
  (render [hello-world]
          (. js/document (getElementById "app"))))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

(defn stop []
  ;; stop is called before any code is reloaded
  ;; this is controlled by :before-load in the config
  (js/console.log "stop"))
