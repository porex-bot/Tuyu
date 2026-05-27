<template>
  <div id="workspaceCommandBar">
    <div class="ws-cmd-inner">
      <span class="ws-cmd-hint">Ctrl+K 快捷命令</span>
    </div>
  </div>
</template>

<style scoped>
#workspaceCommandBar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 40px;
  background: #fff;
  border-top: 1px solid #E5E7EB;
  display: flex;
  align-items: center;
  padding-inline: 20px;
  z-index: 100;
}

.ws-cmd-inner {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}

.ws-cmd-hint {
  font-size: 12px;
  color: #B6BEC9;
}
</style>
