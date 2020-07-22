package com.zjclugger.router;

/**
 * 全局路由常量类<br>
 * Created by King.Zi on 2020/4/9.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class ARouterConstants {

    public class Path {
        /**
         * 登录页面
         */
        public static final String USER_LOGIN = "/app/login";


        //关于报【There is no route match the path】错误的原因分析
        //1.路由路径至少要两级，相同module下的一级路径名称相同，不同module下的一级路径名称不能相同，否则报以上错误
        //2.在【defaultConfig】节点中添加 {
        //         .....
        //        javaCompileOptions {
        //            annotationProcessorOptions {
        //                arguments = [AROUTER_MODULE_NAME: project.getName()]
        //            }
        //        }
        //    }
        //3.在app的【dependencies】中添加依赖
        //====业务组件==========================================================================
        public static final String COM_SELECT_MEDIA = "/component/selectMedia";
        public static final String COM_PREVIEW_MEDIA = "/component/previewMedia";
        public static final String COM_FRAGMENT_ACTIVITY = "/component/fragmentActivity";

        //===模块组件（子系统）=====================================================================
        public static final String MODULE_UPLOAD_BILL = "/module/uploadBill";
        public static final String MODULE_SELECT_DEPART_POST = "/module/selectDepartPost";
    }

    /**
     * 服务名
     */
    public class Service {
        public static final String USER_SERVICE = "/user/service";

        /**
         * 员工列表
         */
        public static final String COM_STAFF_LIST = "/component/StaffListService";
        /**
         * 二维码
         */
        public static final String COM_QR_CODE = "/component/QRCodeService";
    }

    public class KeyWord {
        /**
         * 需要登录
         */
        public static final int NEED_LOGIN = 0x01;
        public static final String KEY_LOGIN_MESSAGE = "relogin_message";

        public static final String KEY_DETAIL_VIEW = "detail_view";
        public static final String KEY_SHOW_EMAIL = "show_email";
        public static final String KEY_COMPANY_ID = "company_id";
        //for media(picture selector)
        public static final String KEY_MEDIA_TITLE_TEXT = "media_title_text";
        public static final String KEY_MEDIA_LIST = "media_list";
        public static final String KEY_MEDIA_LIST_POSITION = "media_list_position";
        public static final String KEY_MEDIA_IS_PICTURE_SMALL = "media_is_picture_small";
        public static final String KEY_MEDIA_IS_READ_ONLY = "media_is_read_only";
        public static final String KEY_MEDIA_IS_SINGLE_LAYOUT = "media_is_single_layout";
        public static final String KEY_MEDIA_GRID_SPAN_COUNT = "media_grid_span_count";
        public static final String KEY_MEDIA_MAX_NUM = "media_max_number";
        public static final String KEY_MEDIA_SELECTED_LIST = "media_selected_list";
        public static final String KEY_MEDIA_PARAMETERS = "media_parameters";
        public static final String KEY_MEDIA_BACKGROUND_COLOR = "media_background_color";
        public static final String KEY_MEDIA_SUB_LAYOUT = "media_sub_layout";
        public static final String KEY_MEDIA_IS_HIDE_ADD_VIEW = "media_is_hide_add_view";
        public static final String KEY_MEDIA_CUSTOM_ADD_VIEW_ID = "media_custom_add_view_id";
        public static final String KEY_MEDIA_IS_ONLY_SUB_LAYOUT = "media_is_only_sub_layout";

        //for FragmentSupportActivity params
        public static final String KEY_FSA_CLASS_NAME = "fsa_class_name";
        public static final String KEY_FSA_FRAGMENT_COLOR = "fsa_fragment_color";
        public static final String KEY_FSA_FRAGMENT_LIGHT = "fsa_fragment_is_light";
        public static final String KEY_FSA_FRAGMENT_PARAMS = "fsa_fragment_params";
    }
}
