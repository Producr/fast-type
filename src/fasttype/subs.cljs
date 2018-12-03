(ns fasttype.subs
  (:require [re-frame.core :refer [reg-sub]]))

;; ------------------------------------------------------
;; LAYER 2 subs
;; ------------------------------------------------------
(reg-sub
  :text-input/value
  (fn [db _]
    (:text-input/value db)))


(reg-sub
  :lesson/line
  (fn [db _]
    (get-in db [:lesson :line])))


(reg-sub
  :lesson/running?
  (fn [db _]
    (:lesson-running? db)))


(reg-sub
  :lesson/mistakes
  (fn [db _]
    (get-in db [:lesson :mistakes])))


(reg-sub
  :lesson/summary
  (fn [db _]
    (get-in db [:lesson :summary])))


(reg-sub
  :lesson/pointer
  (fn [db _]
    (:lesson-pointer db)))


(reg-sub
  :lesson/lessons
  (fn [db _]
    (:lessons db)))


(reg-sub
  :lesson/name
  (fn [db _]
    (get-in db [:lesson :name])))


;; ------------------------------------------------------
;; LAYER 3 subs
;; ------------------------------------------------------
(reg-sub
  :lesson/number
  :<- [:lesson/pointer]
  (fn [lesson-pointer _]
    (inc lesson-pointer)))


(reg-sub
  :lesson/next-lesson-button-disabled?
  :<- [:lesson/pointer]
  :<- [:lesson/lessons]
  :<- [:lesson/running?]
  (fn [[lesson-pointer lessons lesson-running?] _]
    (or (>= lesson-pointer (dec (count lessons)))
        lesson-running?)))


(reg-sub
  :lesson/prev-lesson-button-disabled?
  :<- [:lesson/pointer]
  :<- [:lesson/running?]
  (fn [[lesson-pointer lesson-running?] _]
    (or (= lesson-pointer 0)
        lesson-running?)))
