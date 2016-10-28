package dnn.entity.feedbackInfo.specimen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lgq on 16-9-4.
 * 物理电池
 */
public class PhysicalEnergy extends Battery {

    private String serialNumber; //样品序列号
    private String materialTypes; //单体电池材料类型
    private LocalDateTime physicalEnergySubmitDate; //送样日期

    //单体相关参数
    private String monomerSize; //单体尺寸
    private String monomerThickness;//单体厚度
    private String monomerAcreage; //单体面积
    private String monomerType; //减反射膜类型

    private String tempered ; //(玻璃)钢化,非钢化
    private String temperedThickness;//厚度

    private String seriesBattery;// 串联电池数
    private String parallelBattery;//并联电池数
    private String voltage ; //系统标准电压
    private Boolean parameter; //电气原理图(参数...)
    private Boolean identifying; //标识

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(String materialTypes) {
        this.materialTypes = materialTypes;
    }

    public LocalDateTime getPhysicalEnergySubmitDate() {
        return physicalEnergySubmitDate;
    }

    public void setPhysicalEnergySubmitDate(LocalDateTime physicalEnergySubmitDate) {
        this.physicalEnergySubmitDate = physicalEnergySubmitDate;
    }

    public String getMonomerSize() {
        return monomerSize;
    }

    public void setMonomerSize(String monomerSize) {
        this.monomerSize = monomerSize;
    }

    public String getMonomerThickness() {
        return monomerThickness;
    }

    public void setMonomerThickness(String monomerThickness) {
        this.monomerThickness = monomerThickness;
    }

    public String getMonomerAcreage() {
        return monomerAcreage;
    }

    public void setMonomerAcreage(String monomerAcreage) {
        this.monomerAcreage = monomerAcreage;
    }

    public String getMonomerType() {
        return monomerType;
    }

    public void setMonomerType(String monomerType) {
        this.monomerType = monomerType;
    }

    public String getTempered() {
        return tempered;
    }

    public void setTempered(String tempered) {
        this.tempered = tempered;
    }

    public String getTemperedThickness() {
        return temperedThickness;
    }

    public void setTemperedThickness(String temperedThickness) {
        this.temperedThickness = temperedThickness;
    }

    public String getSeriesBattery() {
        return seriesBattery;
    }

    public void setSeriesBattery(String seriesBattery) {
        this.seriesBattery = seriesBattery;
    }

    public String getParallelBattery() {
        return parallelBattery;
    }

    public void setParallelBattery(String parallelBattery) {
        this.parallelBattery = parallelBattery;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public Boolean getParameter() {
        return parameter;
    }

    public void setParameter(Boolean parameter) {
        this.parameter = parameter;
    }

    public Boolean getIdentifying() {
        return identifying;
    }

    public void setIdentifying(Boolean identifying) {
        this.identifying = identifying;
    }
}
