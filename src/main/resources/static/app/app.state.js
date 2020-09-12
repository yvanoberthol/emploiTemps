angular
.module('myApp')
.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/emploitemps');

  $stateProvider
  // Additional Pages
   .state('403', {
    parent: 'app',
    url: '/403',
    templateUrl: 'app/layouts/pages/403.html'
  })
  .state('404', {
    parent: 'appSimple',
    url: '/404',
    templateUrl: 'app/layouts/pages/404.html'
  })
  .state('500', {
    parent: 'appSimple',
    url: '/500',
    templateUrl: 'app/layouts/pages/500.html'
  })
}]);
