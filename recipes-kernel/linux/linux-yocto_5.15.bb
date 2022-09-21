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

SRCREV_machine_qemuarm ?= "53027a25781dce4876ed47dde4e7bb1f49216bf2"
SRCREV_machine_qemuarm64 ?= "0a49d733a9ec378aa945dbd20f4d1e3b2d4a9052"
SRCREV_machine_qemumips ?= "f8f756c4bfc3489427d714492b29ca316436276d"
SRCREV_machine_qemuppc ?= "1ae9b6880799961c8bc3c33d8b596dbea4002401"
SRCREV_machine_qemuriscv64 ?= "41470215f97ee60487e6c36b562c5b7ecb8a8c7d"
SRCREV_machine_qemuriscv32 ?= "41470215f97ee60487e6c36b562c5b7ecb8a8c7d"
SRCREV_machine_qemux86 ?= "41470215f97ee60487e6c36b562c5b7ecb8a8c7d"
SRCREV_machine_qemux86-64 ?= "41470215f97ee60487e6c36b562c5b7ecb8a8c7d"
SRCREV_machine_qemumips64 ?= "cc95aac05d6fb9f0fbe1c336127568df916e2fed"
SRCREV_machine ?= "41470215f97ee60487e6c36b562c5b7ecb8a8c7d"
SRCREV_meta ?= "529199264800b52ae173fc91241c8e64615850e3"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine_class-devupstream ?= "0e5bb338bf471ec46924f744c4301751bab8793a"
PN_class-devupstream = "linux-yocto-upstream"
KBRANCH_class-devupstream = "v5.15/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.15.43"

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
KERNEL_FEATURES_append_powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES_append_powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES_append_powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP_kernel-vmlinux_qemuppc64 = "textrel"

# devupstream for this case is broken in dunfell and requires later fix
# from commit d0edb03088d0d1c20c899daed1bb3a7110b19670
BBCLASSEXTEND_remove = "devupstream:target"
