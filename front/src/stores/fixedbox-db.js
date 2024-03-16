import { computed} from 'vue'
import { defineStore } from 'pinia'
import { mainApiStore } from '@/stores/main-api';

export const fixedBoxStore = defineStore('fixedBoxData', () => {
  const mainApiStoreInst = mainApiStore();

  const rankTop3 = computed(() => {
    
    if (!mainApiStoreInst.userList || mainApiStoreInst.userList.length === 0) {
      return [];
    }
    const sortedList = [...mainApiStoreInst.userList].sort((a, b) => a.rank - b.rank);

    sortedList.forEach((user, index) => {
      // mainApiStoreInst.userMap에서 사용자 객체를 가져옵니다.
      const userObj = mainApiStoreInst.userMap.get(user.userId);
      if (userObj) {
        userObj.groupRank = index + 1;
        mainApiStoreInst.userMap.set(user.userId, userObj);
      }
    });
    return sortedList;
  });

  const recomProAlgoObject = computed(() => {
    let recomObject = {};

    if(mainApiStoreInst.recomProblemList.length === 0){
      recomObject = mainApiStoreInst.userTierProblemList;
    }else{
      recomObject = mainApiStoreInst.recomProblemList;
    }

    let recomProAndAlgo = {};
    console.log(recomObject)
    recomObject.forEach((value, key) => {
      const proNum = value.problemId;
      console.log(mainApiStoreInst.algorithmMap)
      console.log(proNum)
      const algo= mainApiStoreInst.algorithmMap.get(proNum);
      console.log(algo)
      let algorithmArr = [];
      if(algo){
        algorithmArr = algo.split(" ");
      }
      console.log(algorithmArr)
      recomProAndAlgo[key] = { ...value, algorithm: algorithmArr };
    });
    console.log(recomProAndAlgo)
    return recomProAndAlgo;
  }

  )


  return {
    rankTop3, recomProAlgoObject
  };




});
