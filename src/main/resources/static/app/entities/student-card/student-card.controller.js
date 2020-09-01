(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentCardController', StudentCardController);

    StudentCardController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'StudentCard', '$filter', 'NgTableParams', 'Annee'];

    function StudentCardController ($rootScope, $scope, $stateParams, session, $state, $http,
                                StudentCard, $filter, NgTableParams, Annee) {
        var vm = this;
        vm.loading = true;

        var initialParamsStudentCards = {
            count: 10, // initial page size
            //sorting: { 'name': "asc" }
        };

        vm.fetchCards = function(){
            StudentCard.getAll(session.getAnneeId())
                .then(function(response){
                    vm.loading = false;
                    vm.studentCards = response.data;
                    console.log(vm.studentCards);
                    vm.tableParams = new NgTableParams(initialParamsStudentCards, {
                        counts: [],
                        paginationMaxBlocks: 10,
                        paginationMinBlocks: 2,
                        dataset: vm.studentCards
                    });
            })
        }
        vm.fetchCards();


        vm.updateSelected = function(studentCard){
            if(!studentCard.selected)
            studentCard.selected = !studentCard.selected;
            Annee.updateCarteScolaire(session.getAnneeId(), studentCard.id).then(function(response){
                vm.fetchCards();
            })
        }

    }
})();

