inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

IMAGE_FEATURES += " \
    debug-tweaks \
    tools-profile \
    splash \
    nfs-server \
    tools-debug \
    ssh-server-openssh \
    tools-testapps \
    hwcodecs \
	package-management \
	tools-sdk \
	dev-pkgs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '', \
       bb.utils.contains('DISTRO_FEATURES',     'x11', 'x11-base x11-sato', \
                                                       '', d), d)} \
"
ERPC_COMPS ?= ""
ERPC_COMPS_append_mx7ulp = "packagegroup-imx-erpc"

CORE_IMAGE_EXTRA_INSTALL += " \
    packagegroup-core-full-cmdline \
    packagegroup-tools-bluetooth \
    packagegroup-fsl-tools-audio \
    packagegroup-fsl-tools-gpu \
    packagegroup-fsl-tools-gpu-external \
    packagegroup-fsl-tools-testapps \
    packagegroup-fsl-tools-benchmark \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston-init', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'weston-xwayland xterm', '', d)} \
    ${ERPC_COMPS} \
"

# Use systemd to init system and manage services instead of default sysvinit
DISTRO_FEATURES_append = " systemd"
DISTRO_FEATURES_remove = "sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"

# Enable features since some recipes check this field to automatically build packages supporting this feature via ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then. With this said, systemd will also enable systemd service when building package, x11 enable building x11 version of a program.
# cgroup-lite, aufs and aufs-util required by docker
DISTRO_FEATURES_append = " optee virtualization kvm systemd aufs aufs-util cgroup-lite"

# -------------------------------------------------------
# Packages
# -------------------------------------------------------
# Python related
IMAGE_INSTALL_append = " python-distutils-extra python3-distutils-extra python-pip python3-pip python-setuptools python3-setuptools"

# Dev Utils
IMAGE_INSTALL_append = " git cmake packagegroup-core-buildessential"

# General Utils
IMAGE_INSTALL_append = " htop tmux openssh"

# Package Manager
IMAGE_INSTALL_append = " apt opkg"

# Virtualizations
IMAGE_INSTALL_append = " docker libvirt qemu libxslt"

# removes the problematic gstreamer package included in the default fsl recipe found at `sources/meta-fsl-bsp-release/imx/meta-sdk/recipes-fsl/images/fsl-image-validation-imx.bb`
# CORE_IMAGE_EXTRA_INSTALL_remove = "packagegroup-fsl-gstreamer1.0 packagegroup-fsl-gstreamer1.0-full"