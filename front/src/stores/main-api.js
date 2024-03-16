import { ref, computed } from "vue";
import { groupStore } from "../stores/group";
import { loginStore } from "../stores/loginInfo";
import { defineStore } from "pinia";
import axios from "axios";

export const mainApiStore = defineStore("allData", () => {
  const gStore = groupStore();
  const uStore = loginStore();
  const fullObject = ref({ key: "value" });
  const isLoading = ref(true);

  const selectedGroup = computed(() => {
    return gStore.selectedGroup;
  });
  const loginUser = computed(() => {
    return uStore.loginUser;
  });

  const fetchData = async () => {
    isLoading.value = true;
    console.log(selectedGroup.value.id);
    try {
      axios({
        url: `http://localhost:8080/api/main/group/${selectedGroup.value.id}`,

        method: "GET",
        withCredentials: true,
      }).then((response) => {
        fullObject.value = { ...response.data };
        isLoading.value = false;
      });
    } catch (err) {
      console.log(err);
    }
  };

  const userList = computed(() => {
    if (fullObject.value && fullObject.value.users) {
      return Object.values(fullObject.value.users);
    }

    return []; // fullObject.value.users가 없으면 빈 배열 반환
  });
  const userMap = computed(() => {
    if (userList.value && userList.value.length > 0) {
      return new Map(
        userList.value.map((item, index) => {
          const newItem = { ...item, inTopSolved: [], groupRank: index + 1 };
          return [item.userId, newItem];
        })
      );
    }
    return new Map(); // userList.value가 없으면 빈 Map 반환
  });

  const algorithmList = computed(() => {
    if (fullObject.value && fullObject.value.algorithms) {
      return fullObject.value.algorithms;
    }
    return [];
  });
  const algorithmMap = computed(() => {
    if (!algorithmList.value) return {};
    return new Map(
      algorithmList.value.map((item) => [item.problemNum, item.algorithm])
    );
  });

  const top100problemList = computed(() => {
    if (!fullObject.value || !fullObject.value.top100problems) {
      return [];
    }
    return fullObject.value.top100problems;
  });

  const setUserTop100 = computed(() => {
    if (!fullObject.value || !top100problemList.value) {
      return {};
    }

    const problemsByUserId = top100problemList.value.reduce((acc, element) => {
      // userId와 problemNum을 가져옵니다.
      const { userId, problemId } = element;

      if (!getTop100ProNum.value.includes(problemId)) {
        return acc;
      }

      // 아직 이 userId를 가진 그룹이 없으면 새 그룹을 생성하고, problemNum을 배열에 추가합니다.
      if (!acc[userId]) {
        acc[userId] = [];
      }
      acc[userId].push(problemId);

      return acc;
    }, {});

    return problemsByUserId;
  });

  const getTop100ProNum = computed(() => {
    const uniqueProblemIds = top100problemList.value.reduce((acc, current) => {
      if (!acc.includes(current.problemId)) {
        acc.push(current.problemId);
      }
      return acc;
    }, []);
    return uniqueProblemIds;
  });

  const userTierProblemList = computed(() => fullObject.value.userTierProblems);
  const recomProblemList = computed(() => fullObject.value.recomProblems);

  const userTop100problemList = computed(
    () => fullObject.value.userTop100problems
  );

  return {
    fetchData,
    fullObject,
    userList,
    isLoading,
    setUserTop100,
    userMap,
    top100problemList,
    userTierProblemList,
    recomProblemList,
    algorithmMap,
    userTop100problemList,
    loginUser,
    getTop100ProNum,
  };
});
