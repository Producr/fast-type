(ns fasttype.db)

(def lessons [["Basic Home Row Repeat"
               "aaa aaa aaa aaa aaa aaa aaa aaa"
               "kkk kkk kkk kkk kkk kkk kkk kkk"
               "eee eee eee eee eee eee eee eee"
               "mmm mmm mmm mmm mmm mmm mmm mmm"
               "iii iii iii iii iii iii iii iii"
               "lll lll lll lll lll lll lll lll"
               "uuu uuu uuu uuu uuu uuu uuu uuu"
               "yyy yyy yyy yyy yyy yyy yyy yyy"]
              ["Something else"
               "ueaak  ekake ak eke akea ekaeime"
               "mesangrkkanğ arama nadgrğnk a"]])


(def lesson-defaults {:name            "you should not see this"
                      :line            "you should not see this"
                      :line-pointer    1 ; always skip lesson name line (first line)
                      :char-pointer    0
                      :corrects        0
                      :mistakes        0
                      :correct-history []
                      :mistake-history []
                      :summary         nil})


(def initial-db {:lessons          lessons ; all lessons
                 :lesson           lesson-defaults
                 :lesson-pointer   0
                 :lesson-running?  false
                 :text-input/value ""
                 :settings         {:prevent-errors true}})
