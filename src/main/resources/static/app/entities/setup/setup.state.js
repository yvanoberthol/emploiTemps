(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('setup', {
            parent: 'app',
            abstract: true,
            //url: '/setup',
            params : { view: null },
            data: {
                authorities: ['ADMIN','USER'],
                pageTitle: 'global.menu.account.setup'
            },
            templateUrl: 'app/entities/setup/setup.html',
            controller: 'SetupController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('setup.annee-detail', {
            parent: 'setup',
            url: '/annee-detail',
            data: {
                pageTitle: 'myApp.promo.detail.title'
            },
            templateUrl: 'app/admin/annee/annee-detail.html',
                controller: 'AnneeDetailController',
                controllerAs: 'vm',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('annee');
                        $translatePartialLoader.addPart('promo');
                        return $translate.refresh();
                    }],
                    entity: ['session', 'Annee',  function(session, Annee) {
                         var annee = {};
                         return Annee.get(session.getAnneeId())
                         .then(function(response){
                            annee = response.data;
                            return response.data;
                        })
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'setup',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
        })
        .state('setup.trimestre', {
            parent: 'setup',
            url: '/trimestres',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER'],
                pageTitle: 'myApp.trimestre.home.title'
            },
            templateUrl: 'app/entities/trimestre/trimestres.html',
            controller: 'TrimestreController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trimestre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('setup.student-card', {
            parent: 'setup',
            url: '/carte-scolaire',
            data: {
                authorities: ['ADMIN', 'SUPER', 'USER'],
                pageTitle: 'myApp.trimestre.home.title'
            },
            templateUrl: 'app/entities/student-card/student-cards.html',
            controller: 'StudentCardController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('studentCard');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        ;
    }
})();
