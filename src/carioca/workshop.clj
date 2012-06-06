(ns carioca.workshop
  (:use carioca.game))

; Objetivo final
; ==============
; Defino un nuevo juego simple (un hash con el estado inicial del juego)
(def the-game 
  (new-game  "clojure-boy"))

; Juego dicho juego
(play-game the-game) 

(def the-game 
  (new-game  "clojure-boy" "dr-lazy" "keyword-kid"))




; Godspeed!
; (doc name)
; http://clojure.org/cheatsheet
(doc range)


; Mundo 0 - Caballo's House
; =========================
(+ 1 0)
(def var-inutil 6)
(* 3 var-inutil)
(* 3 2 var-inutil)
; => var-inutil (no cambia)

; => "Hola"
(pr "Hola")
(def hola "Hola ")
(pr hola)
(str hola "iniciado de la clojura")

; => [2 3 4]
(cons 1 [2 3 4])
(conj [2 3 4] 5)

(first [1 2 3])
(rest [1 2 3])

(conj (cons 1 [2 3 4]) 5)

(conj (into [] (cons 1 [2 3 4])) 5)


; Mundo 1 - Sewers
; ================
; Definiciones y tipos

; 1-1 Defina suits & cards
; Tip: Las siguientes funciones pueden ser de utilidad: range, conj
(pr suits)
(pr cards)

; 1-2 Defina player-hand dealer-hand
; Definir funciones




; Mundo 2 - ¡Hay un mundo afuera!
; ===============================
(:players the-game)
(:pile the-game)

; 2-1 Defina player-hand (y dealer-hand)
; Tip: keywords son funciones!
(player-hand the-game "clojure-boy")
(dealer-hand the-game)


; 2-2 Defina human-players
; Tip: funciones de utilidad: filter, string? keys
(human-players the-game)

; Puntajes
(scores the-game)




; Ventanas Disponibles
(play-dialog (dealer-hand the-game) 
             (player-hand the-game "imella") 
             "imella")

(busted-dialog (dealer-hand the-game) (player-hand the-game "imella") "imella")

(busted-dialog (dealer-hand the-game) (player-hand the-game "imella")
               "imella"
               false)

(dealer-dialog (dealer-hand the-game)) 