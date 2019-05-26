#  Filmica


![Swift Version](https://img.shields.io/badge/kotlin-1.3.31-green.svg)

## DESCRIPTION
###Filmica is an app developed with educational purposes. It´s based on The Movie DB API.
### I retrives the dicover films section and the trends films section. You can search on the API
###And last but no least, its main functionality: you can add and delete the movies you want to a Watchlist section.



## NOTES ON THE EXERCISE
This is an exercice for the 8th Mobile Development Bootcamp from KeepCoding.

1. **Trending Films**
	* Fragment almost duplicating FilmsFragment and its view. It was neccesary to implement  a different mutableList on FilmsRepo to avoid mixing items with DiscoverFilms.
	* Refactored the method to fill the list of parsedFilms to receive a parameter to filter the mutable list that will be populated
2. **Films Search**
	* Added a new view with, similar to the discover and trendsfilms RecylcerView, but adding a EditText. Also has a No Results text box.
	* Added the optional parameter of query in the FilmsRepo method, and it´s uri builder.
3. **Trending Films, Watchlist and Search Results Detail View**
	* Trending and search just needed to implement the OnFilmClickListener interface, but it was neccesary to know from what fragment was sent the request. I added another extra data to the intent, thus I can filter from Fragment in FilmsRepo and use the proper list.
	* Watchlist neeed also to attach de fragment.
4. **Undo for deleting and saving films**
	* Deletion: Implemented a Snackbar and an UNDO button that calls to FilmsRepo.saveFilm() method and restores the movie inside the onSwipe method. Also had to call onResume() to refresh the view.
	* Saving: Implemented a Snackbar and UNDO button calling FilmsRepo.deleteFilm() method iside buttonAdd.setOnClickListener
5. **Placeholder for the Detail Section on Landscape tablet**
	* New fragment with its view with a placeholder image, called to replace the detail view from FilmsActivity when needed.
	* Fragment has to be called when changing of menu items.
	* When a watchlist detail is shown and the item is deleted swiping on its item, detail fragment has to be replaced by the placeholder one. Another interface from FragmentDetail is implemented in FilmsActivity to allow calling the fragment replacement if detail is shown an item has been deleted.
6. **Trends and Discover Paging**	 
	**TODO**
	* Este punto me ha resultado muy frustrante.
	* No he sido capaz de llegar a ningún resultado minimamente presentable intentando implementar la librería de paging y me he quedado sin tiempo para tratar de adaptar el gist facilitado.
	* Creo que aún me falta para ser capaz de desarrollar algo así entendiendo lo que hago.
	* He optado por no hacer merge con la rama donde estaba y subir al menos una versión funcional de la práctica hasta el punto 5.
	* Seguiré intentándolo en las próximas semanas


## DEPENDENCIES

```bash
- implementation fileTree(include: ['*.jar'], dir: 'libs')
- implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
- implementation 'com.android.support:appcompat-v7:28.0.0'
- implementation 'com.android.support.constraint:constraint-layout:1.1.3'
- implementation 'com.android.support:recyclerview-v7:28.0.0'
- implementation 'com.android.support:cardview-v7:28.0.0'
- implementation 'com.android.support:design:28.0.0'
- implementation 'com.android.volley:volley:1.1.1'
- implementation 'com.android.support:support-v4:28.0.0'
- implementation 'com.android.support:design:28.0.0'
- implementation 'com.squareup.picasso:picasso:2.5.2'
- implementation 'com.android.support:palette-v7:28.0.0'
- implementation 'android.arch.persistence.room:runtime:1.1.1'
- implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1"
- implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1"
- implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1"
- implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1"
- implementation "android.arch.paging:runtime:1.0.1"
- kapt 'android.arch.persistence.room:compiler:1.1.1'
``` 
