TODO List
=========


Game progression
* [DONE] Intro screen, with key functions and short presentation of mission.  Maybe as overlay on top of planet
* Intra-level screen
* Game over screen
  * If player or planet destroyed
  * Game over sound/music
  * Overlay game over text, send to intro after keypress after a second or so
  * Restart current level


Enemy waves
* A wave creates some enemies, add them over time
* Some enemies follow others
  * Some flight behaviour, formation, or similar components would be nice to have..
* Waves could be generated too
* Waves have a difficulty, get more difficult later
* If a wave + boss has been wiped out 95%, destroy rest of ships, progress to next wave, display text optionally, sound.
* If final wave wiped out, display "sector saved!", win sound/music, get deployed to new planet



Life tracking
<<<<<<< HEAD
* wHealth bar for player
* Health bar for planet
=======
* [DONE] Health bar for player
* [DONE] Health bar for planet
>>>>>>> 97d9a5884bfe07a6c58f753c0532d7ee54c967d7
* Larger explosion for player
* Larger explosion balls and more of them for planet


Sound
* Weapon sounds
* Enemy explosion sounds
* Player death sound
* Planet death sound
* Bonus pickup sound
* Title screen music
* Death music
* Game music?


Powerups
* Some enemies may drop their weapon when destroyed, it is picked up and equipped if flown over
* When picked up, show "picked up: weapon name" or soemthing like that
* Weapons have their own sounds also..
* Some enemies spawn healthpacks
* (Some enemies might spawn mines?)
* Possibly other powerups than weapons or health - engine boosts etc - for later expansion


Ship rendering
* Rotate the player ship based on direction and angle
* Rotate enemy bombers based on direction to planet
* Rotate enemy fighters based on flight direction
* Calculate player velocity, use it for predictive camera


Planet damage indication
* Destroyed pixels or such?  Not that necessary, leave until later
* But make explosion effects when a shot hits the planet, and planet hit sound


Controls
* [DONE] Zoom with scrollwheel also, limit min and max zoom
* [DONE] Scaling camera

Space rendering
* Some stars / stardust in the background?



