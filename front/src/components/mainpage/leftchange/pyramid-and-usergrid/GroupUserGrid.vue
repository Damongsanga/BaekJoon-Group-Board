<template>
    <div class="flex grid items-center justify-center grid-cols-10 gap-3">
        <!-- UserTile 컴포넌트를 사용하여 사용자 목록을 표시 -->
        <UserTile v-for="user in mainApiStoreInst.userList" class="z-30" :key="user.userId"
            :src="user.profileImageUrl || 'https://static.solved.ac/misc/360x360/default_profile.png'"
            :alt="user.userName || 'Default Name'" @onTileClick="handleTileClick(user.userId)"
            @mouseover="(event) => handleMouseOver(user.handle, event)" @mouseleave="handleMouseLeave" />

        <MiniProfile v-if="showMiniProfile" :userName="currentUserName" :style="{ top: `${mouseY}px`, left: `${mouseX}px` }"
            class="z-40 mini-profile-container" />

    </div>
</template>

<script setup>
import { ref } from 'vue';
import UserTile from '@/components/mainpage/leftchange/pyramid-and-usergrid/usergrid/UserTile.vue';
import MiniProfile from '@/components/mainpage/leftchange/pyramid-and-usergrid/usergrid/MiniProfile.vue';

// Pinia 스토어를 사용하는 경우 (예시)
import { mainApiStore } from '@/stores/main-api';
import { selectedUserStore } from '@/stores/userInfo';

const mainApiStoreInst = mainApiStore();
const userInfoInst = selectedUserStore();

const showMiniProfile = ref(false);
const currentUserName = ref('uhiu');
const mouseX = ref(0);
const mouseY = ref(0);

const handleMouseOver = (userName, event) => {
    console.log(userName)
    currentUserName.value = userName;
    mouseX.value = event.pageX;
    mouseY.value = event.pageY - 60;
    showMiniProfile.value = true;
};


const handleMouseLeave = () => {
    showMiniProfile.value = false;
};

const handleTileClick = (userId) => {
    userInfoInst.userId = userId;
};
</script>
