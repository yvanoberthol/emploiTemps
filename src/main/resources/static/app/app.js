angular
.module('myApp', [
  'ui.router',
  'ngAnimate',
  'ui.bootstrap'
])

.config(function($locationProvider) {
      $locationProvider.html5Mode(true);
})

//Possibly unhandled rejection with Angular 1.5.9 #2889
.config(['$qProvider', function ($qProvider) {
      $qProvider.errorOnUnhandledRejections(false);
}]);

