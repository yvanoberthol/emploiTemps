(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('ReglementController', ReglementController);

    ReglementController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Reglement', '$filter', 'NgTableParams', 'Promo'];

    function ReglementController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Reglement, $filter, NgTableParams, Promo) {
        var vm = this;
        vm.loading = true;
        vm.updateStats = updateStats;

        Promo.getByAnnee(session.getAnneeId())
        .then(function(response){
            vm.promos = response.data;
        })

        var patternDateTime = "YYYY-MM-DD HH:mm";
        var patternDate = "YYYY-MM-DD";

        var fromLabel = "Du";
        var toLabel = "Au";
        var applyLabel = "Valider";
        var cancelLabel = "Annuler";
        var customRangeLabel = "Autre";
        var todayLabel = "Aujourd'hui";
        var yesterdayLabel = "Hier";
        var thisWeekLabel = "Cette Semaine";
        var thisMonthLabel = "Ce Mois";
        var thisYearLabel = "Cette Année";
        var daysOfWeek =  ["Di", "Lu", "Ma", "Me", "Je", "Ve", "Sa" ];
        var monthNames = [ "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
                        "Julliet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre" ]

        //default range
        vm.range = {startDate: moment().startOf('year'), endDate: moment().endOf('year')};
        vm.updateStats();

        var ranges = {};
        //ranges[todayLabel] = [moment().startOf('day'), moment().endOf('day')];
        //ranges[yesterdayLabel] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
        //ranges[thisWeekLabel] = [moment().startOf('isoweek'), moment().endOf('isoweek')];
        ranges[thisMonthLabel] = [moment().startOf('month'), moment().endOf('month')];
        ranges[thisYearLabel] = [moment().startOf('year'), moment().endOf('year')];

        vm.options = {
          applyClass: 'btn-success',
          locale: {
            applyLabel: applyLabel,
            fromLabel: fromLabel,
            format: "DD/MM/YYYY", //will give you 2017-01-06
            toLabel: toLabel,
            cancelLabel: cancelLabel,
            customRangeLabel: customRangeLabel,
            "daysOfWeek": daysOfWeek,
            "monthNames": monthNames,
            "firstDay": 1
          },
          ranges: ranges,
          eventHandlers: {
            'hide.daterangepicker': function() {
                vm.updateStats();
            }
          }
        }

        vm.promoChanged = function(){
            vm.promo =  $filter('filter')(vm.promos, {id: vm.promoId})[0];
            vm.tableParams.reload();
        }

        function updateStats(){
            vm.dateFrom = vm.range.startDate.format(patternDate);
            vm.dateTo = vm.range.endDate.format(patternDate);
            vm.dateTimeFrom = vm.range.startDate.format(patternDateTime);
            vm.dateTimeTo = vm.range.endDate.format(patternDateTime);

            var initialParams = {
                count: 10, // initial page size
                sorting: { 'createdDate': "desc" }
            };

            vm.tableParams = new NgTableParams(initialParams, {
                counts: [],
                paginationMaxBlocks: 10,
                paginationMinBlocks: 2,

                getData: function(params) {
                    if(params.sorting()){
                        for(var sortBy in params.sorting()){
                            vm.sortBy = sortBy;
                            vm.direction = params.sorting()[sortBy];
                        }
                    }

                    if(!vm.promoId)
                    vm.promoId = 0;
                    return Reglement.getByAnnee(params.page()-1, params.count(), vm.sortBy, vm.direction,
                                    vm.dateTimeFrom, vm.dateTimeTo, session.getAnneeId(), vm.promoId)
                    .then(function(response){
                        params.total(response.data.totalElements);
                        vm.reglements = response.data.content;
                        vm.loading = false;
                        return vm.reglements;
                    })
                }
            });
        }
    }
})();

