(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('emploitemps', {
            url: '/emploitemps',
            templateUrl: 'app/entities/emploitemps/emploitemps.html',
            controller: 'EmploiTempsController',
            controllerAs: 'vm'
        })
    }
})();
