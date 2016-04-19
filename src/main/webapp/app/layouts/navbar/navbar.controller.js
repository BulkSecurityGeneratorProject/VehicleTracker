(function() {
    'use strict';

    angular
        .module('vehicleTrackerApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope', '$location', '$state', 'Auth', 'Principal', 'ENV', 'LoginService'];

    function NavbarController ($scope, $location, $state, Auth, Principal, ENV, LoginService) {
        var vm = this;

        vm.navCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.inProduction = ENV === 'prod';
        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;
        vm.account = null;

        function login () {
            LoginService.open();
        }

        function logout () {
            Auth.logout();
            vm.account = null;
            $state.go('home');
        }

        getAccount();
        $scope.$on('authenticationSuccess', function(){
            vm.account = getAccount();
        });

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
    }
})();
