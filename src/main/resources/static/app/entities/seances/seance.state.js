(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('seanceByTeacher', {
            url: '/seance-by-teacher/:teacher',
            templateUrl: 'app/entities/seances/seances-by-teacher.html',
            controller: 'SeanceController',
            controllerAs: 'vm'
        })
    }
})();
