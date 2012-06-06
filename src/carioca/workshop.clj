(ns carioca.workshop
  (:use [carioca.game :as g]))

; Objetivo final
; ==============
; Defino un nuevo juego simple (un hash con el estado inicial del juego)
(def the-game 
  (g/new-game  "clojure-boy"))

; Juego dicho juego
(g/play-game the-game) 

(def the-game 
  (g/new-game  "clojure-boy" "dr-lazy" "keyword-kid"))




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

(conj 
  (cons 1 [2 3 4]) 5)

(conj 
  (into [] (cons 1 [2 3 4])) 
  5)

(def rng 
  (range 1 6))

(inc 5)

(map inc rng)
(type (map inc rng))

(reduce + rng)

(reduce + (map inc rng))

(count rng)
(take 3 rng)

(take 3 (map inc rng))
(first (map inc rng))

; no me ejecutes!
(cycle rng)
(repeat 4)

(take 10 (cycle rng))
(take 5 (repeat :x))

(defn mul5 [x] (* x 5))
(mul5 5)

; danger!
(iterate mul5 1)

(nth (iterate mul5 1) 3)

; pa que def
(nth (iterate (fn [x] (* x 5)) 1) 4)

(if (= 2 3) 
  "Muerte y desesperacion!" 
  ":)")

; ¿Donde esta mi viejo for?
(for [x (range 1 10) :when (odd? x)] x)

; o mas simplemente...
(filter odd? (range 1 10))




; Mundo 1 - Sewers
; ================
; Definiciones y tipos

; 1-1 Defina suits & cards
; Tip: Las siguientes funciones pueden ser de utilidad: range, conj
(pr g/suits)
(pr g/cards)


; 1-2 Obtener todas las cartas de una pinta de a pares 
; {2 :spades, :queen :spades, :king :spades, 3 :spades, ... }
; Tip: Revisa: zipmap repeat

; 1-3 BOSS FIGHT: Crea un mazo
; g/deck
; Tip: Revisa: for




; Mundo 2 - Zapatos
; =================
; Definicion de funciones simples

(:players the-game)
(:pile the-game)

; 2-1 Defina player-hand
; (g/player-hand the-game "clojure-boy")


; 2-2 Defina dealer-hand ocupando la "funcion" :dealer
; (g/dealer-hand the-game)


; 2-3 BOSS FIGHT: Obten los nombres de los jugadores "humanos"
; Tip: revisa: filter, string?, keys
; (g/human-players the-game)




; Mundo 3 - It's time to play the game
; ====================================
; Necesitaras las definiciones de las funciones previas

; Ventanas Disponibles
(g/play-dialog (dealer-hand the-game) 
             (player-hand the-game "clojure-boy") 
             "clojure-boy")

(g/dealer-dialog (dealer-hand the-game))


; Puntajes
(g/scores the-game)

; Turno de un jugador
(g/play-player-turn the-game "clojure-boy")

; Turno del dealer (auto)
(g/play-dealer-turn the-game)

; 3-1 Implemente el turno de todos los jugadores de forma hermosa
; Tip: Recuerde reduce
; (g/play-players-turns the-game)


; 3-2 BOSS FIGHT: Implemente la funcion del juego entero
; Tip: ¿Recuerde? ->
; (g/play-game the-game)
