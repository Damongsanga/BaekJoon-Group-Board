import { ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";
import axios from "axios";

export const loginStore = defineStore("loginInfo", () => {
  const loginUser = ref({});
  const router = useRouter();

  const login = async (userName) => {
    try {
      const response = await axios({
        url: `http://localhost:8080/api/user-group/login/${userName}`,
        method: "GET",
        withCredentials: true,
      });

      console.log(response.data);
      loginUser.value = response.data;
      localStorage.setItem('user', JSON.stringify(response.data));
      if (response.data) {
        router.replace({ name: "group" });
      }
    } catch (err) {
      alert("오류가 발생하였습니다. 담비한테 물어보세요.");
      console.log(err.message);
    }
  };

  const regist = async (userName) => {
    try {
      const response = await axios({
        url: `http://localhost:8080/api/user-group/user/add/${userName}`,
        method: "GET",
        withCredentials: true,
      });

      // 서버에서 유저 등록이 비동기적으로 작동하기 때문에 response가 항상 void, 200으로 와서 alert이 제대로 안뜸
      if (response.data.status == "226") {
        alert("이미 등록된 유저입니다.");
      } else if (response.data.status == "400") {
        alert("더이상 등록할 수 없습니다.");
      } else {
        alert("등록 완료되었습니다. 로그인해주세요");
      }
    } catch (err) {
      alert("오류가 발생하였습니다. 담비한테 물어보세요.");
      console.log(err.message);
    }
  };

  return { login, loginUser, regist };
});
