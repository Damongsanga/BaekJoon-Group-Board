<template>
   <nav class="fixed z-30 w-full h-20 bg-black border-b border-gray-200">
      <div class="w-full h-full px-3 py-3 lg:px-5 lg:pl-3">
         <div class="flex items-center justify-between w-full h-full">
            <LogoTitle />
            <GroupName />
            <LoginUserInfo />

         </div>
      </div>
   </nav>
</template>
<script>
import GroupName from '@/components/mainpage/header/items/GroupName.vue';

import LogoTitle from '@/components/mainpage/header/items/LogoTitle.vue';
import LoginUserInfo from '@/components/mainpage/header/items/LoginUserInfo.vue';



export default {
   components: { LogoTitle, GroupName, LoginUserInfo }
}
</script>