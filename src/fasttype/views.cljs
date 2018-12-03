(ns fasttype.views)

;; Utility functions for ease of use for re-frame
(def <sub (comp deref re-frame.core/subscribe))
(def >evt re-frame.core/dispatch)


;; ------------------------------------------------------
;; STYLES
;; ------------------------------------------------------
(def lesson-font-size "2.5rem")
(def lesson-font-family "'Roboto Mono', monospace")
(def lesson-padding-left "2rem")


;; ------------------------------------------------------
;; COMPONENTS
;; ------------------------------------------------------
(defn header []
  [:div.row
   [:div.col.s12
    [:h1.center-align {:style {:margin 0}} "Fast Type"]]])


(defn tagline []
  [:div.row
   [:div.col.s12
    [:p.center-align {:style {:margin 0}}
     [:i "\"Fast type I do; but you do not.\" - Master Yoda"]]]])


(defn start-stop-button []
  (let [running (<sub [:lesson/running?])
        lesson-no (<sub [:lesson/number])]
    [:div.col
     [:button.btn {:on-click #(>evt (if running [:lesson/stop] [:lesson/start]))
                   :class    (if running "red" "")}
      (if running "Stop Lesson" (str "Start Lesson " lesson-no))]]))


(defn lesson-name []
  [:div.col
   [:span {:style {:font-size "2rem"}}
    (str (<sub [:lesson/number]) "- " (<sub [:lesson/name]))]])


(defn lesson-selector-input []
  [:div.col
   [:input {:type      "text"
            :size      1
            :on-change #(>evt [:lesson/select-lesson (-> % .-target .-value)])
            :style     {:margin-bottom 0
                        :font-size     "1.5rem"
                        :position      "relative"
                        :bottom        "0.7rem"}}]])


(defn next-lesson-button []
  [:div.col
   [:button.btn {:on-click #(>evt [:lesson/select-next-lesson])
                 :disabled (<sub [:lesson/next-lesson-button-disabled?])} ">"]])


(defn prev-lesson-button []
  [:div.col
   [:button.btn {:on-click #(>evt [:lesson/select-prev-lesson])
                 :disabled (<sub [:lesson/prev-lesson-button-disabled?])} "<"]])


(defn mistakes []
  (when (<sub [:lesson/running?])
    [:div.col
     [:p {:style {:font-size "1rem"}} (str "Mistakes: ")
      [:b (<sub [:lesson/mistakes])]]]))


(defn lesson-text []
  [:p.blue-grey.lighten-5 {:style {:font-family   lesson-font-family ;
                                   :font-size     lesson-font-size
                                   :margin-bottom "1rem"
                                   :padding-left  lesson-padding-left}}
   (<sub [:lesson/line])])


(defn text-input []
  [:input#text-input {:type        "text"
                      :value       (<sub [:text-input/value])
                      :on-key-down #(>evt [:text-input/key-down (.-key %)])
                      :on-change   #()
                      :style       {:font-family  lesson-font-family
                                    :font-size    lesson-font-size
                                    :padding-left lesson-padding-left}
                      :class       (if (<sub [:lesson/running?]) "teal lighten-5")
                      :disabled    (not (<sub [:lesson/running?]))}])


(defn lesson-summary []
  (let [{:keys [mistakes accuracy total-keystrokes total-time CPM] :as summary} (<sub [:lesson/summary])]
    (when summary
      [:div
       [:h5 "Lesson Summary:"]
       [:div.row
        [:h6.col [:b "Accuracy: "] (str accuracy "%")]
        [:h6.col [:b "Mistakes: "] (str mistakes "/" total-keystrokes)]
        [:h6.col [:b "Total Time: "] (str total-time "s")]
        [:h6.col [:b "CPM: "] (str CPM)]
        [:h6.col [:b "WPM: "] (str (Math/round (/ CPM 4)))]]])))


;; ------------------------------------------------------
;; MAIN UI component - every other component will be
;; a child of this.
;; ------------------------------------------------------
(defn ui []
  [:div.container
   [header]
   [tagline]
   [:div.row
    [start-stop-button]
    [lesson-name]
    [lesson-selector-input]
    [prev-lesson-button]
    [next-lesson-button]
    [mistakes]]
   [lesson-text]
   [text-input]
   [lesson-summary]])


