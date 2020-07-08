(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeController', AnneeController);

    AnneeController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Annee', '$filter', 'NgTableParams'];

    function AnneeController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Annee, $filter, NgTableParams) {
        var vm = this;
        vm.loading = true;

        vm.loading = true;

        var initialParamsAnnees = {
            count: 10, // initial page size
            sorting: { 'label': "desc" }
        };

        Annee.getAll()
            .then(function(response){
                vm.loading = false;
                vm.annees = response.data;
                console.log(vm.annees);
                vm.tableParams = new NgTableParams(initialParamsAnnees, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.annees
                });
        })


        vm.updateActive = function(annee){
            annee.active = !annee.active;
            Annee.update(annee);
        }
    }
})();

