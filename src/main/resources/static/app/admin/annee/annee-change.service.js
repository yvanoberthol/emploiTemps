(function() {
    'use strict';

    angular
        .module('myApp')
        .factory('AnneeChangeService', AnneeChangeService);

    AnneeChangeService.$inject = ['$uibModal', '$rootScope'];

    function AnneeChangeService ($uibModal, $rootScope) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };
        return service;

        function open () {
            if (modalInstance !== null) return;
            console.log("annee change open")

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/admin/annee/annee-change-dialog.html',
                controller: 'AnneeChangeDialogController',
                controllerAs: 'vm',
                backdrop: 'false',
                size: 'md',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('annee');
                        return $translate.refresh();
                    }]
                }
            });

            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
