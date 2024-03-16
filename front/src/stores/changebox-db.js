import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { mainApiStore } from '@/stores/main-api';

export const changeboxStore = defineStore('changeBox', () => {
  const mainApiStoreInst = mainApiStore()
const overProblemNum = ref(0);


const getProblemObj = computed(() => {
  // mainApiStoreInst.top100problemList에서 조건에 맞는 첫 번째 요소를 찾습니다.
  const foundElement = mainApiStoreInst.top100problemList.find(element => {
    return element.problemId === overProblemNum.value;
  });
  if(!foundElement)return [];

  const algo= mainApiStoreInst.algorithmMap.get(overProblemNum.value);
  const algorithmArr = algo.split(" ");
  const obj = { ...foundElement, algorithm: algorithmArr };
  // 찾은 요소를 반환하거나, 없으면 빈 배열을 반환합니다.
  return obj;
});


  return {
    overProblemNum,getProblemObj
  }
})