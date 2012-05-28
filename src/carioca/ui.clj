(ns carioca.ui
  (:use seesaw.core)
  (:import (javax.swing JFrame JLabel JButton)))

(native!)

(defn owl
  [c g]
  (let [img (javax.imageio.ImageIO/read 
      (java.net.URL. "http://web.me.com/chrisdiss3/SuffolkOwlSanctuary/files/european_owl_1_467x313.jpg"))]    
    (draw g (.getGraphics img))))


(def f 
  (frame 
    :title "Get to know Seesaw"
    :content (button 
               :icon (icon (java.net.URL. "http://web.me.com/chrisdiss3/SuffolkOwlSanctuary/files/european_owl_1_467x313.jpg")))))

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



