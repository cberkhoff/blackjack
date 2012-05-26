(ns carioca.ui
  (:use seesaw.core)
  (:import (javax.swing JFrame JLabel JButton)))

(native!)

(def f (frame :title "Get to know Seesaw"))
(-> f pack! show!)

(def b (button :text "Click me!"))
(alert "Shit")
(input "Color?")


(defn create-crappy-ui
  []
  (doto (JFrame. "Carioca")
    (.add (JLabel. "trolo"))
    (.setSize 400 400)
    (.setVisible true)))
