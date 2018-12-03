# fast-type
A ClojureScript learning project created from scratch.
Depends on Shadow-CLJS and Re-Frame.

You can add touch typing lessons tailored specific to your needs.

## Notes
Step by step setup: 
- Install Shadow-CLJS via npm/yarn if you don't already have it installed.
- This project is set up via Cursive editor. Cursive doesn't currently support resolving dependencies from `shadow-cljs.edn` so you have to use Leiningen (project.clj file) for now. The project is already set up like this. 
- Run `shadow-cljs server` from the command line to start up the server.
- Create a remote REPL in Cursive for localhost:7000 (you can change the port in `shadow-cljs.edn`). Connect to the repl. In the REPL run:
    ```clojure
    (require '[shadow.cljs.devtools.api :as shadow])
    (shadow/watch :app)
    (shadow/nrepl-select :app)
    ```
  This will turn your REPL into a ClojureScript one and Shadow-CLJS will start watching your build and reload any changes you make.
- Copy resources/index.html to target/index.html since Shadow-CLJS doesn't do this automatically.  


## License
Copyright © 2018 Producr

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
