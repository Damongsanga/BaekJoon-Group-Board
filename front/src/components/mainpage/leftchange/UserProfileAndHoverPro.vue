<template>
    <div class="flex flex-col h-full gap-10">
        <UserProfile />
        <HoverPro />
    </div>
</template>

<script>
import UserProfile from '@/components/mainpage/leftchange/userprofile-and-hoverpro/UserProfile.vue';
import HoverPro from '@/components/mainpage/leftchange/userprofile-and-hoverpro/HoverPro.vue';

export default {
    components: {
        UserProfile,
        HoverPro,
    }
}
</script>
