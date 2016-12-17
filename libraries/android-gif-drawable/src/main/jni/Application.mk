APP_ABI := arm64-v8a x86_64
APP_PLATFORM := android-23

ifeq ($(NDK_DEBUG),1)
	APP_OPTIM := debug
else
	APP_OPTIM := release
endif

NDK_TOOLCHAIN_VERSION := clang