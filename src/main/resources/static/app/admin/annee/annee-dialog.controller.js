(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeDialogController', AnneeDialogController);

    AnneeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Annee'];

    function AnneeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Annee) {
        var vm = this;
        vm.annee = entity;
        console.log(vm.annee);

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            vm.isSaving = true;
            if (vm.annee.id !== null) {
                Annee.update(vm.annee, onSaveSuccess, onSaveError);
            } else {
                Annee.save(vm.annee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:anneeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.anneeActiveChanged = function(){
            if(!vm.annee.active)
                vm.annee.showOnStoreFront = false;
        }
    }
})();
