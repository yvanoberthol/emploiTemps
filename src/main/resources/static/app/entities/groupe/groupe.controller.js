(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupeController', GroupeController);

    GroupeController.$inject = ['$scope', '$stateParams', 'session', '$state', '$http', 'Groupe', 'NgTableParams'];

    function GroupeController ($scope, $stateParams, session, $state, $http, Groupe, NgTableParams) {
        var vm = this;
        vm.loading = true;

        var initialParamsGroupes = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        Groupe.getAll()
            .then(function(response){
                vm.loading = false;
                vm.groupes = response.data;
                //console.log(vm.groupes);
                vm.tableParams = new NgTableParams(initialParamsGroupes, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.groupes
                });
        })

        vm.editGroupe = editGroupe;
        function editGroupe(id){
            $state.go('groupe.edit', {groupeId:id});
        }

        vm.update = function(groupe){
            groupe.enabled = !groupe.enabled;
            Groupe.update(groupe);
        }
    }
})();

