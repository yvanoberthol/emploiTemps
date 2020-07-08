(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('login', {
          parent: 'appSimple',
          url: '/login',
          templateUrl: 'app/account/login/login.html',
          controller: 'LoginController as vm',
          resolve: {
             mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                 $translatePartialLoader.addPart('global');
                 $translatePartialLoader.addPart('login');
                 return $translate.refresh();
             }]
          }
        })
    }

})();
