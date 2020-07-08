(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('AnneeChangeDialogController', AnneeChangeDialogController);

    AnneeChangeDialogController.$inject = ['$rootScope', '$scope', '$http', 'auth', 'session', '$state',
                                            '$uibModalInstance', 'Annee', '$filter'];

    function AnneeChangeDialogController ($rootScope, $scope, $http, auth, session, $state,
                                            $uibModalInstance, Annee, $filter) {
        var vm = this;
        vm.clear = clear;
        vm.change = change;

        function clear () {
           $uibModalInstance.dismiss();
        }

        Annee.getAll()
        .then(function(response){
            vm.annees = response.data;
            if(session.getAnnee().id){
                vm.annee =  $filter('filter')(vm.annees, {id: session.getAnnee().id})[0];
            }
        })

        function change () {
           session.setAnnee(vm.annee);
           $state.go('home', {}, {reload: true});
           $uibModalInstance.dismiss();
        }
    }
})();
