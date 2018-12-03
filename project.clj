(defproject fasttype "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0-beta8"]
                 [org.clojure/clojurescript "1.10.439"]
                 [thheller/shadow-cljs "2.7.3"]
                 [re-frame "0.10.6"]
                 #_[day8.re-frame/re-frame-10x "0.3.3-react16"]
                 #_[re-frisk "0.5.4"]]

  :repl-options {:init-ns shadow.main
                 :nrepl-middleware
                          [shadow.cljs.devtools.server.nrepl/cljs-load-file
                           shadow.cljs.devtools.server.nrepl/cljs-eval
                           shadow.cljs.devtools.server.nrepl/cljs-select
                           ;; required by some tools, not by shadow-cljs.
                           cemerick.piggieback/wrap-cljs-repl]})
