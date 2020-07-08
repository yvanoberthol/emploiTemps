angular
.module('myApp')
.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/');

  $stateProvider
  .state('app', {
    abstract: true,
    templateUrl: 'app/layouts/full.html',
    controller: 'NavbarController as vm',
    //page title goes here
    resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('global');
        }]
    }
  })
  .state('appSimple', {
    abstract: true,
    templateUrl: 'app/layouts/simple.html',
    controller: 'NavbarController as vm',
    resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('global');
        }]
    }
  })

  /*.state('admin', {
    abstract: true,
    templateUrl: 'app/layouts/admin.html',
    controller: 'NavbarAdminController as vm',
    resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('global');
        }]
    }
  })*/

  // Additional Pages
   .state('403', {
    parent: 'app',
    url: '/403',
    templateUrl: 'app/layouts/pages/403.html',
    resolve: {
         mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
             $translatePartialLoader.addPart('global');
             return $translate.refresh();
         }]
    }
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
