(ns fasttype.events
  (:require [re-frame.core :as rf]
            [fasttype.db :as db]))


;; ------------------------------------------------------
;; HELPER FUNCTIONS
;; ------------------------------------------------------
(defn lesson-setup [{:keys [lesson-pointer] :as db}]
  (-> db
      (assoc-in [:lesson :name] (first (get db/lessons lesson-pointer)))
      (assoc-in [:lesson :line] (second (get db/lessons lesson-pointer)))))


;; ------------------------------------------------------
;; EFFECTS
;; ------------------------------------------------------


;; ------------------------------------------------------
;; COEFFECTS
;; ------------------------------------------------------
(rf/reg-cofx
  :now
  (fn [cofx _]
    (assoc cofx :now (.now js/Date))))


;; ------------------------------------------------------
;; APP-DB events
;; ------------------------------------------------------
(rf/reg-event-db
  :app-db/init
  (fn [_ _]
    (lesson-setup db/initial-db)))


;; ------------------------------------------------------
;; LESSON events
;; ------------------------------------------------------
(rf/reg-event-fx
  :text-input/key-down
  [(rf/inject-cofx :now)]
  (fn [{:keys [db now]} [_ key]]
    (when (:lesson-running? db)
      (let [lesson-line (get-in db [:lesson :line])
            pointer (get-in db [:lesson :char-pointer])
            next-char (nth lesson-line pointer)]
        ;(println "next char: " next-char " entered: " key " pointer: " pointer)
        (if (= next-char key)
          {:dispatch-n [[:text-input/correct key now]
                        (when (=  pointer (dec (count lesson-line)))
                          [:lesson/next-line])]}
          {:dispatch [:text-input/mistake key next-char now]})))))


(rf/reg-event-db
  :text-input/correct
  (fn [db [_ key time]]
    ;(println "Correct. Key: " key " time: " time)
    ;; log the key
    (-> db
        (update-in [:lesson :char-pointer] inc)
        (update :text-input/value #(str % key))
        (update-in [:lesson :corrects] inc)
        (update-in [:lesson :correct-history] #(conj % [key time])))))


(rf/reg-event-db
  :text-input/mistake
  (fn [db [_ key correct-key time]]
    ;(println "Incorrect. Key: " key ", should have been: " correct-key "time: " time)
    ;;TODO ignore modifier keys like SHIFT etc, log every other (proper) mistake
    (-> db
        (update-in [:lesson :mistakes] inc)
        (update-in [:lesson :mistake-history] #(conj % [key correct-key time])))))


(rf/reg-event-fx
  :lesson/next-line
  (fn [{:keys [db]} _]
    ;(println "Should go to next line")
    (let [line-pointer (get-in db [:lesson :line-pointer])
          cur-lesson (nth (:lessons db) (:lesson-pointer db))]
      (if (= line-pointer (dec (count cur-lesson)))
        {:dispatch [:lesson/stop]}
        {:db (-> db
                 (assoc-in [:lesson :char-pointer] 0)
                 (update-in [:lesson :line-pointer] inc)
                 (assoc-in [:lesson :line] (nth cur-lesson (inc line-pointer)))
                 (assoc :text-input/value ""))}))))


(rf/reg-event-fx
  :lesson/start
  (fn [{:keys [db]} _]
    {:db
     (-> db
         (assoc-in [:lesson :summary] nil)
         (assoc :lesson-running? true))
     :dispatch [:lesson/reset]}))


(rf/reg-event-fx
  :lesson/stop
  (fn [{:keys [db]} _]
    ;(println "Stopping lesson")
    {:db (-> db
           (assoc :lesson-running? false)
           (assoc :text-input/value ""))
     :dispatch [:lesson/summary]}))


(rf/reg-event-db
  :lesson/summary
  (fn [db _]
    (let [corrects (get-in db [:lesson :corrects])
          mistakes (get-in db [:lesson :mistakes])
          total-keystrokes (+ corrects mistakes)
          correct-start-time (or (second (first (get-in db [:lesson :correct-history])))
                                 0)
          mistake-start-time (or (last (first (get-in db [:lesson :mistake-history])))
                                 correct-start-time)
          start-time (min correct-start-time mistake-start-time)
          ;; No need to find the max between latest mistake and latest correct because
          ;; we'll normally end on a correct key press
          end-time (or (second (last (get-in db [:lesson :correct-history])))
                       0)
          total-time (/ (- end-time start-time) 1000) ; turned into seconds from milliseconds
          CPM (Math/round (* (/ corrects total-time) 60))] ; characters per minute
      (-> db
          (assoc-in [:lesson :summary :total-keystrokes] total-keystrokes)
          (assoc-in [:lesson :summary :total-time] total-time)
          (assoc-in [:lesson :summary :mistakes] mistakes)
          (assoc-in [:lesson :summary :CPM] (if (js/isNaN CPM) 0 CPM))
          (assoc-in [:lesson :summary :accuracy] (.toFixed (* (/ corrects total-keystrokes) 100) 2))))))


(rf/reg-event-db
  :lesson/reset
  (fn [db _]
    (-> db
        (assoc :lesson db/lesson-defaults)
        (lesson-setup))))


(rf/reg-event-db
  :lesson/select-lesson
  (fn [db [_ lesson-no]]
    ;(println "lesson no: " lesson-no)
    (if (and (> lesson-no 0)
             (<= lesson-no (count (:lessons db)))
             (not (:lesson-running? db)))
      (-> db
          (assoc :lesson-pointer (dec lesson-no))
          (lesson-setup))
      db)))


(rf/reg-event-db
  :lesson/select-next-lesson
  (fn [db _]
    (if (and (< (:lesson-pointer db) (dec (count (:lessons db))))
             (not (:lesson-running? db)))
      (-> db
        (update :lesson-pointer inc)
        (lesson-setup))
      db)))


(rf/reg-event-db
  :lesson/select-prev-lesson
  (fn [db _]
    (if (and (> (:lesson-pointer db) 0)
             (not (:lesson-running? db)))
      (-> db
          (update :lesson-pointer dec)
          (lesson-setup))
      db)))
