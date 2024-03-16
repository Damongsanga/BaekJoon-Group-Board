import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

import { mainApiStore } from '@/stores/main-api';

export const selectedUserStore= defineStore('pickUser', () => {
  const mainApiStoreInst = mainApiStore()
  const userId = ref(0);

  const getUserMap = computed(() => 
  mainApiStoreInst.userMap.get(userId.value));

  const userName = computed(() => {
    const user = getUserMap.value;
    return user ? user.handle : '';
  });

  const userTier = computed(() => {
    const user = getUserMap.value;
    return user ? user.tier : null;
  });

  const userRank = computed(() => {
    const user = getUserMap.value;
    return user ? user.rank : 0;
  });

  const groupRank = computed(()=>{
    const user = getUserMap.value;
    return user ? user.groupRank : 0;
  })

  const userProfileImg = computed(() => {
    const user = getUserMap.value;
    return user ? user.profileImageUrl : 0;
  });

  const userCnt = computed( () => {
    
    const solvedCnt = mainApiStoreInst.top100problemList.filter( (element) => 
    element.userId === userId.value
    ).length;
    
    return solvedCnt;
  })



  return { userProfileImg, userId, userName, userTier, userRank, groupRank, userCnt};

})