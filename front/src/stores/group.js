
import { computed, ref } from "vue";
import { defineStore } from "pinia";
import { loginStore } from "@/stores/loginInfo";
import axios from "axios";

export const groupStore = defineStore("group", () => {
  const lStore = loginStore();

  const groupList = ref([]);
  const groupUserMap = ref({});
  const isLoading = ref(true);
  const selectedGroup = ref({});

  const loginUser = ref({});


  const fetchGroupList = async () => {
    isLoading.value = true;
    try {
      const response = await axios({
        url: "http://localhost:8080/api/user-group/group",
        method: "GET",
        withCredentials: true,
      });
      groupList.value = response.data;


      await fetchLoginUser();

      // 여기서 비동기 함수를 사용하여 각 그룹에 대한 사용자 정보를 가져오도록 수정
      Promise.all(groupList.value.map((group) => fetchUsers(group.id))).then(
        () => {

          isLoading.value = false;
          console.log("완료");
        }
      );
    } catch (err) {
      console.error(err.message);
    }
  };

  const fetchUsers = async (groupId) => {
    try {
      const response = await axios({
        url: `http://localhost:8080/api/user-group/group/admin/${groupId}`,
        method: "GET",
        withCredentials: true,
      });
      console.log("로딩중");
      groupUserMap.value[groupId] = response.data;
      console.log("로딩완료");
    } catch (err) {
      console.log(err.message);
    }
  };


  const fetchLoginUser = async () => {
    try {
      const response = await axios({
        url: `http://localhost:8080/api/user-group/user/loginuser`,
        method: "GET",
        withCredentials: true,
      });
      console.log("로그인 유저");
      console.log(response.data);
      loginUser.value = response.data;
    } catch (err) {
      console.log(err.message);
    }
  };

  const leaveGroup = async (groupId) => {
    if (!window.confirm("정말로 그룹을 나가시겠습니까?")) {
      return;
    }

    try {
      const response = await axios({
        url: `http://localhost:8080/api/user-group/group/leave/${groupId}`,
        method: "GET",
        withCredentials: true,
      });

      fetchGroupList();
    } catch (err) {
      console.log(err.message);
    }
  };

  return {
    fetchGroupList,
    fetchUsers,
    groupList,
    groupUserMap,
    leaveGroup,
    isLoading,
    loginUser,
    fetchLoginUser,

  };
});
