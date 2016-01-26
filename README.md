# PopularMovies
Application project in Android for the Udacity course:
https://www.udacity.com/course/developing-android-apps--ud853

Required Components:

A. User Interface - Layout

-Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails

-UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by:
  most popular, highest rated

-UI contains a screen for displaying the details for a selected movie

-Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

B. User Interface - Function

-When a user changes the sort criteria (“most popular and highest rated”)
  the main view gets updated correctly.

-When a movie poster thumbnail is selected, the movie details screen is launched

C. Network API Implementation 

-In a background thread, app queries the /discover/movies API with the query parameter
  for the sort criteria specified in the settings menu.
  (Note: Each sorting criteria is a different API call.)

-This query can also be used to fetch the related metadata needed for the detail view.

App conforms to common standards found in the Android Nanodegree General Project Guidelines:
http://udacity.github.io/android-nanodegree-guidelines/core.html
