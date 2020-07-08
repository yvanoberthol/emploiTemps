(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('register', {
          parent: 'appSimple',
          url: '/register',
          templateUrl: 'app/account/register/register.html',
          controller: 'RegisterController as vm',
          resolve: {
             mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                 $translatePartialLoader.addPart('global');
                 $translatePartialLoader.addPart('register');
                 return $translate.refresh();
             }]
          }
        })
    }

})();
