<template>
    <div
        :class="['p-5 text-gray-200 border border-white rounded-lg shadow sm:p-6 xl:p-8', userInfoInst.userId === 0 ? 'invisible' : '']">
        <div>
            <UserDetailInfo class="bg-black" />
        </div>
    </div>
</template>

<script>
import UserDetailInfo from './userprofile/UserDetailInfo.vue';
import { selectedUserStore } from '@/stores/userInfo';
import { mainApiStore } from '@/stores/main-api';


export default {
    components: { UserDetailInfo },
    setup() {
        const mainApiStoreInst = mainApiStore();
        const userInfoInst = selectedUserStore();


        const setHidden = function () {
            return "{'invisible': userInfoInst.userId === 0}"
        }

        return {
            userInfoInst,
            mainApiStoreInst,
            setHidden

        };
    }
}
</script>
