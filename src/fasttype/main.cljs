(ns fasttype.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [fasttype.views :as v]
            [fasttype.events]
            [fasttype.subs]))

(comment
  @re-frame.db/app-db

  (rf/dispatch-sync [:app-db/init]))


;; ------------------------------------------------------
;; Entry-point from shadow-cljs
;; ------------------------------------------------------
(defn main! []
  (rf/dispatch-sync [:app-db/init])
  (reagent/render v/ui (.getElementById js/document "app")))

;; ------------------------------------------------------
;; Function that is called from shadow-cljs on reload.
;; ------------------------------------------------------
(defn reload! []
  (println "Reloaded")
  (reagent/render v/ui (.getElementById js/document "app")))

