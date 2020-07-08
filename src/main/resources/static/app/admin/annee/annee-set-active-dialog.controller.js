(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeSetActiveController',AnneeSetActiveController);

    AnneeSetActiveController.$inject = ['$uibModalInstance', 'entity', 'session', 'Annee'];

    function AnneeSetActiveController($uibModalInstance, entity, session, Annee) {
        var vm = this;

        vm.annee = entity;
        vm.clear = clear;
        vm.confirmSetActive = confirmSetActive;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmSetActive (id) {
            Annee.setActive(id)
            .then(function(response){
                onSaveSuccess(response);
            }, function(){
                onSaveError();
            });
        }

        function onSaveSuccess (response) {
            $uibModalInstance.close(response);
            session.setAnnee(vm.annee);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
