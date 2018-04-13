Ont été faites :

Interface PlateauJeu:
  	    *coupsPossibles
  	    *joue
  	    *copy
  	    *coupValide
	    *finDePartie
  
Interface Partiel
  	    * saveToFile (sous réserve d'implémentation de toString())



To do :

/!\\ TODO 14/04 : (re )faire les tests, implémentation de l'interface avec l'arbitre + interface avec les String, et faire une javadoc propre
     
Interface Partiel
  *   EstMoveValide (à redéfinir)
  *   MouvementsPossibles (vérifier comment est foutue la chaine représentant un coup)
  *   EstChoixValide (c/c la moitié de MoveValide courant)
  *   ChoixPossibles
  *   fonction mystère	
  *   play(str str)


en substance, le principal changement à implémenter est la différenciation dans les interfaces entre les dons et les dépôts.