<template>
  <div id="app">
    <a-config-provider :locale="zhCN" :theme="antTheme">
      <BasicLayout />
    </a-config-provider>
  </div>
</template>

<script setup lang="ts">
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import type { ThemeConfig } from 'ant-design-vue';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
dayjs.locale('zh-cn');

import BasicLayout from '@/layouts/BasicLayout.vue'

const antTheme: ThemeConfig = {
  token: {
    colorPrimary: '#3B82A0',
    colorSuccess: '#34A853',
    colorWarning: '#F59E0B',
    colorError: '#E05555',
    colorInfo: '#6B8EA4',
    colorTextBase: '#1F2933',
    colorBgBase: '#FFFFFF',
    colorBgLayout: '#F7F8FA',
    colorBgContainer: '#FFFFFF',
    colorBorder: '#E5E7EB',
    colorBorderSecondary: '#EEF0F3',
    borderRadius: 6,
    borderRadiusLG: 8,
    borderRadiusSM: 4,
    fontSize: 14,
    fontSizeHeading1: 24,
    fontSizeHeading2: 20,
    fontSizeHeading3: 16,
    fontSizeLG: 16,
    controlHeight: 36,
    controlHeightLG: 44,
    controlHeightSM: 28,
    lineHeight: 1.5,
    fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif",
    boxShadow: '0 1px 3px rgba(0, 0, 0, 0.06)',
    boxShadowSecondary: '0 4px 12px rgba(0, 0, 0, 0.08)',
    wireframe: false,
  },
  components: {
    Layout: {
      headerBg: '#FFFFFF',
      headerHeight: 56,
      bodyBg: '#F7F8FA',
      siderBg: '#F3F5F7',
      triggerBg: '#F3F5F7',
    },
    Menu: {
      itemBg: 'transparent',
      itemSelectedBg: '#EEF5F7',
      itemSelectedColor: '#3B82A0',
      itemHoverBg: '#E2EFF2',
      itemColor: '#4B5563',
      itemHeight: 40,
      iconSize: 18,
      collapsedIconSize: 18,
      horizontalLineHeight: 56,
    },
    Card: {
      paddingLG: 16,
      borderRadiusLG: 8,
    },
    Button: {
      primaryShadow: 'none',
      defaultShadow: 'none',
      dangerShadow: 'none',
      borderRadius: 6,
      controlHeight: 36,
      controlHeightLG: 44,
      controlHeightSM: 28,
    },
    Input: {
      borderRadius: 6,
      controlHeight: 36,
      controlHeightLG: 44,
    },
    Tag: {
      borderRadiusSM: 4,
      defaultBg: '#F3F5F7',
      defaultColor: '#4B5563',
    },
    Pagination: {
      itemActiveBg: '#3B82A0',
      itemActiveColorDisabled: '#FFFFFF',
    },
    Table: {
      headerBg: '#F7F8FA',
      headerColor: '#4B5563',
      borderColor: '#EEF0F3',
      rowHoverBg: '#F7F8FA',
    },
    Tabs: {
      inkBarColor: '#3B82A0',
      itemActiveColor: '#3B82A0',
      itemHoverColor: '#3B82A0',
      itemSelectedColor: '#3B82A0',
    },
    Progress: {
      defaultColor: '#3B82A0',
      remainingColor: '#E5E7EB',
    },
    Select: {
      borderRadius: 6,
      controlHeight: 36,
    },
    Modal: {
      borderRadiusLG: 10,
    },
    Breadcrumb: {
      itemColor: '#8A94A6',
      lastItemColor: '#4B5563',
      linkColor: '#8A94A6',
      linkHoverColor: '#3B82A0',
      separatorColor: '#B6BEC9',
    },
  },
};
</script>

<style scoped></style>
