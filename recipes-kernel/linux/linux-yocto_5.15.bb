KBRANCH ?= "v5.15/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.15/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.15/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.15/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.15/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.15/standard/base"
KBRANCH_qemuriscv32  ?= "v5.15/standard/base"
KBRANCH_qemux86  ?= "v5.15/standard/base"
KBRANCH_qemux86-64 ?= "v5.15/standard/base"
KBRANCH_qemumips64 ?= "v5.15/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "d685426cf1b93301b84f6b3dbc991f597fd706ab"
SRCREV_machine_qemuarm64 ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_machine_qemumips ?= "6ff6cd4991c2d99a4814dc6ad054efef7a04d63e"
SRCREV_machine_qemuppc ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_machine_qemuriscv64 ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_machine_qemuriscv32 ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_machine_qemux86 ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_machine_qemux86-64 ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_machine_qemumips64 ?= "b9298c1913cfa71d93adb1da7aec3af9e11c9de0"
SRCREV_machine ?= "3f365e00205a8bb6d2161c55d6ad93139fc6279e"
SRCREV_meta ?= "0255d115fd41140e340604d0203e14d8095b176a"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE_class-devupstream = "-1"
SRCREV_machine_class-devupstream ?= "760a85303c5a2aeb811f92c76b8dca4c13bf3416"
PN_class-devupstream = "linux-yocto-upstream"
KBRANCH_class-devupstream = "v5.15/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.15.15"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"

# devupstream for this case is broken in dunfell and requires later fix
# from commit d0edb03088d0d1c20c899daed1bb3a7110b19670
BBCLASSEXTEND_remove = "devupstream:target"
