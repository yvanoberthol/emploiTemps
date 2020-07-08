(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentDetailController', StudentDetailController);

    StudentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                    'session', 'Promo', 'Student'];

    function StudentDetailController($scope, $rootScope, $stateParams, previousState, entity,
                                    session, Promo, Student) {
        var vm = this;
        vm.student = entity;
        vm.save = save;
        //vm.student.dateNaissance = moment(vm.student.dateNaissance, "DDMMYYYY").format("DD/MM/YYYY")

        vm.previousState = previousState.name;
        vm.selected = 0;
        vm.selectedTab = 0;
        vm.saveError= null;
        vm.saveSuccess = null;

        vm.getSolde = function(index){
            if(index > 0){
                return (vm.student.mouvements[index].amount + vm.getSolde(index-1));
            }
            else return vm.student.mouvements[index].amount;
        }

        vm.tabSelected = function(index){
            vm.selectedTab = index;
            vm.ligneTrimestre = vm.student.notes[index];
            vm.saveError= null;
            vm.saveSuccess = null;
        }
        vm.tabSelected(0);

        var key = Object.keys(vm.student.notes)[0];
        var matieres = vm.student.notes[key]
        vm.tabSelected(0, matieres);

        function save () {
            vm.isSaving = true;
            Student.update(vm.student, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.saveError= false;
            vm.saveSuccess = true;
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.saveError= true;
            vm.saveSuccess = false;
        }

    }
})();
