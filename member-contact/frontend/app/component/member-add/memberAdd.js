
angular.module('memberApp').component('memberAdd', {
  templateUrl: 'component/member-add/member-add-view.html',
  controller: memberAddController
});

memberAddController.$inject = ['MemberResource'];
function memberAddController(MemberResource) {
  var vm = this;
  vm.member={};
  vm.member.contacts=[];

  vm.addContact = function (){
    vm.member.contacts.push({});
  };
  vm.removeContact = function(contact){
    var index = vm.member.contacts.indexOf(contact);
    vm.member.contacts.splice(index,1);
  };

  vm.addMember = function() {
    var onSuccess = function(successResponse) {
      vm.successMsg = successResponse.name + ' successfully added.';
      vm.successId = successResponse.id;
    };

    var onFailure = function (errorResponse) {
      vm.errorMsg = errorResponse.statusText + '(' + errorResponse.data + ')';
    };

    MemberResource.save({
      name: vm.member.name,
      contacts: vm.member.contacts
    }, onSuccess, onFailure);
  }
}