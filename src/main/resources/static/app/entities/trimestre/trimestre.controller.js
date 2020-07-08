(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('TrimestreController', TrimestreController);

    TrimestreController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Trimestre', '$filter', 'NgTableParams'];

    function TrimestreController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Trimestre, $filter, NgTableParams) {
        var vm = this;
        vm.loading = true;

        vm.loading = true;

        var initialParamsTrimestres = {
            count: 10, // initial page size
            //sorting: { 'name': "asc" }
        };

        Trimestre.getByAnnee(session.getAnneeId())
            .then(function(response){
                vm.loading = false;
                vm.trimestres = response.data;
                console.log(vm.trimestres);
                vm.tableParams = new NgTableParams(initialParamsTrimestres, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.trimestres
                });
        })


        vm.updateEnabled = function(trimestre){
            trimestre.enabled = !trimestre.enabled;
            Trimestre.update(trimestre);
        }
    }
})();

