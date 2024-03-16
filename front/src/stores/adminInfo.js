
import { ref, computed } from "vue";

import { defineStore } from "pinia";
import { groupStore } from "../stores/group";
import { useRouter } from "vue-router";
import axios from "axios";

export const adminStore = defineStore("adminStore", () => {
  const gStore = groupStore();
  const allUserList = ref([]);
  const groupUserList = ref([]); // groupStore에서 가져오기
  const router = useRouter();

  const selectedGroup = computed(() => {
    return gStore.selectedGroup;
  });
  const reset = () => {
    console.log(selectedGroup.value.id);

    getAllUser();
    getGroupUser();
  };

  // group : groupId
  // password : password
  const adminLogin = async (password) => {
    try {
      const jsonData = {

        group: selectedGroup.value.id,

        password: password,
      };

      const response = await axios({
        url: `http://localhost:8080/api/user-group/group/admin`,
        method: "POST",
        withCredentials: true,
        data: jsonData,
      });

      console.log(response.data);

      if (response.data) {
        router.push({ name: "admin" });
      } else router.push({ name: "group" });
    } catch (err) {
      console.error(err.message);
    }
  };

  const getAllUser = async () => {
    try {
      const response = await axios({
        url: "http://localhost:8080/api/user-group/users",
        method: "GET",
        withCredentials: true,
      });
      allUserList.value = response.data;
    } catch (err) {
      console.error(err.message);
    }
  };

  const getGroupUser = async () => {

    console.log(selectedGroup.value.id);
    try {
      axios({
        url: `http://localhost:8080/api/user-group/group/admin/${selectedGroup.value.id}`,

        method: "GET",
        withCredentials: true,
      }).then((response) => {
        groupUserList.value = response.data;
      });
    } catch (err) {
      console.log(err.message);
    }
  };

  // jsonData
  // "group" : groupId
  // "user" : userId

  const removeUser = async (userId) => {
    if (
      !window.confirm(
        `정말로 ${
          groupUserList.value.find((user) => user.userId === userId).handle
        }님을 내보내시겠습니까?`
      )
    ) {
      return;
    }

    const jsonData = {

      group: selectedGroup.value.id,

      user: userId,
    };

    try {
      const response = await axios({
        url: "http://localhost:8080/api/user-group/group/admin/remove-user",
        method: "POST",
        withCredentials: true,
        data: jsonData,
      });

      if (response.data.status == "400") {
        alert("오류가 발생하였습니다. 담비한테 물어보세요.");
      } else {
        alert(`유저가 방출되었습니다.`);
      }
    } catch (err) {
      console.error(err.message);
    } finally {
      reset();
    }

    // 다시 페이지로 돌아가야함
  };

  // jsonData
  // "group" : groupId
  // "user" : userId

  const addUser = async (userId) => {
    if (
      !window.confirm(
        `${
          allUserList.value.find((user) => user.userId === userId).handle
        }님을 그룹에 추가하시겠습니까?`
      )
    ) {
      return;
    }

    const jsonData = {

      group: selectedGroup.value.id,

      user: userId,
    };

    try {
      const response = await axios({
        url: "http://localhost:8080/api/user-group/group/admin/add-user",
        method: "POST",
        withCredentials: true,
        data: jsonData,
      });

      if (response.data.status == "400") {
        alert("오류가 발생하였습니다. 담비한테 물어보세요.");
      }
    } catch (err) {
      console.error(err.message);
    } finally {
      reset();
    }

    // 다시 페이지로 돌아가야함
  };

  const deleteGroup = async () => {
    try {
      const response = await axios({

        url: `http://localhost:8080/api/user-group/group/admin/${selectedGroup.value.id}`,

        method: "DELETE",
        withCredentials: true,
      });

      if (response.data.status === 400) {
        alert("오류가 발생하였습니다. 담비한테 물어보세요.");
      } else {
        alert(`그룹이 정상적으로 삭제되었습니다.`);
        router.push({ name: "group" });
      }
    } catch (err) {
      console.error(err.message);
    }
  };

  return {
    reset,
    allUserList,
    groupUserList,
    getAllUser,
    getGroupUser,
    removeUser,
    addUser,
    deleteGroup,
    adminLogin,
    selectedGroup,
  };
});
